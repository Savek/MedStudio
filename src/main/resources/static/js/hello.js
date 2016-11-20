angular.module('main', [ 'ngRoute' ])
    .config(function($routeProvider, $httpProvider) {

        $routeProvider.when('/', {
            templateUrl : 'home.html',
            controller : 'home',
            controllerAs: 'controller'
        }).when('/login', {
            templateUrl : 'login.html',
            controller : 'navigation',
            controllerAs: 'controller'
        }).otherwise('/');

        $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

    })
    .controller('home', function($rootScope, $http, $location) {
        var self = this;
        $http.get('/user').then(function(response) {
            self.userInfo = {};

            self.userInfo.name = response.data.name;
        })
    })
    .controller('navigation', function($rootScope, $http, $location) {

        var self = this;
        self.tab = function(route) {
            return $route.current && route === $route.current.controller;
        };

        var authenticate = function(credentials, callback) {

            var data = "";
            if (credentials) {
                data = "username="+credentials.username+"&password="+credentials.password+"&submit=Login";
            }

            $http({
                method: 'POST',
                url: '/login',
                data: data,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                }
            })
            .success(function(data, status, headers, config){
                $http.get('user', {
                    headers : headers
                }).then(function(response) {

                    if (response.data.name) {
                        $rootScope.authenticated = true;
                    } else {
                        $rootScope.authenticated = false;
                    }
                    callback && callback($rootScope.authenticated);

                }, function() {
                    $rootScope.authenticated = false;
                    $location.path("/login");
                    self.error = true;
                    callback && callback(false);
                });
            })
            .error(function(data, status, headers, config){
                $location.path("/login");
                self.error = true;
            })
        }

        authenticate();

        self.credentials = {};
        self.login = function() {
            authenticate(self.credentials, function(authenticated) {
                if (authenticated) {
                    $location.path("/");
                    self.error = false;
                    $rootScope.authenticated = true;
                } else {
                    $location.path("/login");
                    self.error = true;
                    $rootScope.authenticated = false;
                }
            })
        };
        self.logout = function() {
            $http.post('logout', {}).finally(function() {
                $rootScope.authenticated = false;
                $location.path("/");
            });
        }
    });