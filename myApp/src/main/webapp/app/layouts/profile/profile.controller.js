(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('ProfileController', ProfileController);

    ProfileController.$inject = ['Principal'];

    function ProfileController (Principal) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
    }
})();
