const { contextBridge, ipcRenderer } = require('electron');

contextBridge.exposeInMainWorld('electronAPI', {
  saveLog: (logEntry) => ipcRenderer.invoke('save-log', logEntry),
  toggleClickThrough: (value) => ipcRenderer.send('toggle-click-through', value),
  setOpacity: (opacity) => ipcRenderer.send('set-opacity', opacity),
  openSettings: () => ipcRenderer.send('open-settings'),
  onMuteStatus: (callback) => ipcRenderer.on('mute-status', (event, value) => callback(value)),
  getAppPath: () => ipcRenderer.invoke('get-app-path'),
  getDesktopSources: () => ipcRenderer.invoke('get-desktop-sources'),
  hideWindow: () => ipcRenderer.send('hide-window'),
});
