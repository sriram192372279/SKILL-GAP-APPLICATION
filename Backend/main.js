const { app, BrowserWindow, globalShortcut, ipcMain, screen, session } = require('electron');
const path = require('path');
const fs = require('fs');

let mainWindow;
let settingsWindow;
let isClickThrough = false;
let isMuted = false;

function createMainWindow() {
  const { width, height } = screen.getPrimaryDisplay().workAreaSize;

  mainWindow = new BrowserWindow({
    width: 400,
    height: 600,
    x: width - 420,
    y: 50,
    frame: false,
    transparent: true,
    alwaysOnTop: true,
    resizable: true,
    skipTaskbar: false,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js'),
      nodeIntegration: false,
      contextIsolation: true,
    },
  });

  mainWindow.loadFile(path.join(__dirname, 'src/index.html'));
  mainWindow.setMenuBarVisibility(false);
  mainWindow.setAlwaysOnTop(true, 'screen-saver');
}

function createSettingsWindow() {
  if (settingsWindow) {
    settingsWindow.focus();
    return;
  }

  settingsWindow = new BrowserWindow({
    width: 500,
    height: 700,
    title: "AI Interview Copilot Settings",
    webPreferences: {
      preload: path.join(__dirname, 'preload.js'),
    },
  });

  settingsWindow.loadFile(path.join(__dirname, 'src/settings.html'));
  settingsWindow.on('closed', () => settingsWindow = null);
}

function registerShortcuts() {
  globalShortcut.register('CommandOrControl+Shift+H', () => {
    if (mainWindow) {
      if (mainWindow.isVisible()) {
        mainWindow.hide();
      } else {
        mainWindow.show();
      }
    }
  });

  globalShortcut.register('CommandOrControl+Shift+M', () => {
    isMuted = !isMuted;
    mainWindow.webContents.send('mute-status', isMuted);
  });

  globalShortcut.register('CommandOrControl+Shift+Q', () => {
    app.quit();
  });
}

app.whenReady().then(() => {
  // Handle permission requests
  session.defaultSession.setPermissionRequestHandler((webContents, permission, callback) => {
    const allowedPermissions = ['media', 'audioCapture', 'desktopCapture'];
    if (allowedPermissions.includes(permission)) {
      console.log(`Permission granted: ${permission}`);
      callback(true);
    } else {
      console.warn(`Permission denied: ${permission}`);
      callback(false);
    }
  });

  createMainWindow();
  registerShortcuts();

  app.on('activate', function () {
    if (BrowserWindow.getAllWindows().length === 0) createMainWindow();
  });
});

app.on('window-all-closed', function () {
  if (process.platform !== 'darwin') app.quit();
});

ipcMain.handle('save-log', async (event, logEntry) => {
  const logPath = path.join(app.getPath('userData'), 'interview_log.json');
  let logs = [];
  if (fs.existsSync(logPath)) {
    const data = fs.readFileSync(logPath);
    logs = JSON.parse(data);
  }
  logs.push({ ...logEntry, timestamp: new Date().toISOString() });
  fs.writeFileSync(logPath, JSON.stringify(logs, null, 2));
  return { success: true, path: logPath };
});

ipcMain.on('toggle-click-through', (event, value) => {
  isClickThrough = value;
  if (mainWindow) {
    mainWindow.setIgnoreMouseEvents(isClickThrough, { forward: true });
  }
});

ipcMain.on('hide-window', () => {
  if (mainWindow) mainWindow.hide();
});

ipcMain.on('open-settings', () => {
  createSettingsWindow();
});

ipcMain.on('set-opacity', (event, opacity) => {
  if (mainWindow) {
    mainWindow.setOpacity(opacity);
  }
});

ipcMain.handle('get-app-path', () => app.getAppPath());

ipcMain.handle('get-desktop-sources', async () => {
  const { desktopCapturer } = require('electron');
  const sources = await desktopCapturer.getSources({ types: ['window', 'screen'] });
  return sources.map(source => ({
    id: source.id,
    name: source.name,
    thumbnail: source.thumbnail.toDataURL(),
  }));
});
