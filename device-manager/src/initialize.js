'use strict';
var fetch = require("node-fetch");
const config = require('../config');

const service = (req, res) => {
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
}

module.exports = {
    service: service
}