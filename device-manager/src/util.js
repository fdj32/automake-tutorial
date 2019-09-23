'use strict';

const hexChar = c => {
    var out = c.toString(16);
    return (1 === out.length ? "0" : "") + out;
}

const leftPadding0 = (num, length) => {
    for (var len = (num + "").length; len < length; len = num.length) {
        num = "0" + num;
    }
    return num;
}

const hexOutput = s => {
    var out = "Offset  ";
    for (var i = 0; i < 16; i++) {
        out += hexChar(i) + " ";
    }
    out += "\n";
    var line = 0;
    for (var i = 0; i < s.length; i++) {
        if (i % 16 === 0) {
            line++;
            out += "\n" + leftPadding0(line, 6) + "  ";
        }
        out += hexChar(s.charCodeAt(i)) + " ";
    }
    console.log(out);
}

module.exports = {
    hexChar: this.hexChar,
    hexOutput: hexOutput
}