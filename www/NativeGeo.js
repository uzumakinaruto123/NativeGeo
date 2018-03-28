var exec = require('cordova/exec');
var PLUGIN_NAME = 'NativeGeo';

var NativeGeo = {
    getCurrentLocation: function(success, error) {
        exec(success, error, "NativeGeo", "getCurrentLocation", []);
    }
};

module.exports = NativeGeo;