const SerialPort = require('serialport')
SerialPort.list().then(PortInfos => {
	console.log(PortInfos)
})