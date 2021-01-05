'use strict';
const protocol = require('../src/protocol');
const cpx = require('../src/cpx');
const util = require('../src/util');
var msg = protocol.cpx58display("Hello", "    Nick", "Well done", "  President Xi");
console.log(msg);
console.log(msg.length);
util.hexOutput(msg, 10);
const SerialPort = require('serialport')
SerialPort.list().then(PortInfos => {
	console.log(PortInfos)
});

var d = new cpx.Cpx58display({mode: '0'});
util.hexOutput(d.format(), 16);

const port = new SerialPort('/dev/tty.usbmodem14101')

port.write(msg, function(err) {
  if (err) {
    return console.log('Error on write: ', err.message)
  }
  console.log('message written')
})
