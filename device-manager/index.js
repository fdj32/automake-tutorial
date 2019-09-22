'use strict';

const config = require('./config');

const express = require('express');

var fetch = require("node-fetch");

// Constants
const PORT = 8080;
const HOST = '0.0.0.0';

// App
const app = express();
app.get('/', (req, res) => {
  res.send('Hello world\n');
});

app.get('/service/initialize', (req, res) => {
  var merchantUser = req.query.merchantUser;
  var merchantPassword = req.query.merchantPassword;
  var terminalNumber = req.query.terminalNumber;
  var paymentDeviceSerialNumber = req.query.paymentDeviceSerialNumber;
  var pd = config.amsHost + "?tenderType=EMV&transactionType=EMV_PARAM&merchantUser=" + merchantUser;
  pd += "&merchantPassword=" + merchantPassword;
  pd += "&terminalNumber=" + terminalNumber;
  pd += "&paymentDeviceSerialNumber=" + paymentDeviceSerialNumber;
  console.log(pd);
  fetch(pd).then(response => response.text()).then(response => {
    console.log(response);
    res.setHeader("Content-Type", "text/plain");
    res.write(response);
    res.end();
  });
});

app.listen(PORT, HOST);
console.log(`Running on http://${HOST}:${PORT}`);