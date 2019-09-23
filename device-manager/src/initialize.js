'use strict';
const fetch = require("node-fetch");
const xml2js = require("xml2js");
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
        //console.log(response);
        xml2js.parseString(response, (err, result) => {
            console.log(JSON.stringify(result));
        });
        res.setHeader("Content-Type", "text/plain");
        res.write(response);
        res.end();
    });
}

module.exports = {
    service: service
}