
angular
    .module('main')
    .controller('loginController',
        function($rootScope, $http, $location, AuthService) {

            $rootScope.isNavCollapsed = true;
            $rootScope.isAuthenticated = AuthService.isAuthenticated();

            var self = this;

            self.credentials = {};
            self.login = function() {

                AuthService.login(self.credentials)
                    .then(function () {
                        $rootScope.isAuthenticated = AuthService.isAuthenticated();
                        if ($rootScope.isAuthenticated) {
                            $location.path("/");
                        }
                });
            };

            self.logout = function() {

                AuthService.logout()
                    .then(function() {
                        $rootScope.isAuthenticated = AuthService.isAuthenticated();
                        $location.path("/");
                    })
            }
        }
    );