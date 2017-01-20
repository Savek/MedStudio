/**
 * Created by Savek on 2017-01-14.
 */
angular
    .module('util', ['ngCookies'])
    .factory('AuthService', function ($http, $location, $route, Session) {
        var authService = {};

        authService.login = function (credentials) {

            var headers = credentials ? {authorization : "Basic "
                + btoa(credentials.username + ":" + credentials.password )
                } : {};

            return $http
                .get('/user', {headers : headers})
                .then(function (res) {
                    if (res.data.authenticated) {
                        Session.create(res.data.name, res.data.name);
                    }
                });
        };

        authService.isAuthenticated = function () {
            return !!Session.id;
        };

        authService.logout = function () {
            return $http.post('logout', {}).finally(function() {
                Session.destroy();
                $route.reload();
            });
        };

        return authService;

    }).service('Session', function () {

        this.create = function (sessionId, userId) {
            this.id = sessionId;
            this.userId = userId;
        };

        this.destroy = function () {
            this.id = null;
            this.userId = null;

        };
    });