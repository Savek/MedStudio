
angular
    .module('main')
    .controller('loginController',
        function($rootScope, $scope, $http, $location, AuthService) {

            $rootScope.isNavCollapsed = true;
            $rootScope.isAuthenticated = AuthService.isAuthenticated();

            $scope.succes = null;
            var self = this;

            self.credentials = {};
            self.login = function() {

                AuthService.login(self.credentials)
                    .then(function () {
                        $rootScope.isAuthenticated = AuthService.isAuthenticated();
                        if ($rootScope.isAuthenticated) {
                            $location.path("/");
                        }
                    }, function() {
                        $scope.succes = false;
                    });
            };

            AuthService.login();

            self.logout = function() {

                AuthService.logout()
                    .then(function() {
                        $rootScope.isAuthenticated = AuthService.isAuthenticated();
                        $location.path("/");
                    })
            };

        }
    );