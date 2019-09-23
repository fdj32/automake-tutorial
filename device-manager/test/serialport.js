'use strict';
const protocol = require('../src/protocol');
const util = require('../src/util');
var msg = protocol.cpx58display("Hello", "    Nick", "Well done", "  President Xi");
console.log(msg);
console.log(msg.length);
util.hexOutput(msg);
const SerialPort = require('serialport')
SerialPort.list().then(PortInfos => {
	console.log(PortInfos)
});

const port = new SerialPort('/dev/tty.usbmodem14101')

port.write(msg, function(err) {
  if (err) {
    return console.log('Error on write: ', err.message)
  }
  console.log('message written')
})
