/**
 * Created by Savek on 2017-01-14.
 */
angular
    .module('util', ['ngCookies'])
    .factory('AuthService', function ($http, $location, $cookies, $route, Session) {
        var authService = {};

        authService.login = function (credentials) {

            var headers = credentials ? {authorization : "Basic "
                + btoa(credentials.username + ":" + credentials.password )
                } : {};

            return $http
                .get('/user', {headers : headers})
                .then(function (res) {
                    if (res.data.authenticated) {
                        Session.create(res.data.name, res.data.name,
                            res.data.authorities);
                    }
                });
        };

        authService.isAuthenticated = function () {
            if (!$cookies.get("sessionMedStudio")) {
                Session.destroy();
            }
            return !!$cookies.get("sessionMedStudio");
        };

        authService.logout = function () {
            return $http.post('logout', {}).finally(function() {
                Session.destroy();
                $route.reload();
            });
        };

        return authService;

    }).service('Session', function ($cookies) {

        this.create = function (sessionId, userId, userRole) {
            this.id = sessionId;
            this.userId = userId;
            this.userRole = userRole;

            var today = new Date();
            var expiresValue = new Date(today);
            expiresValue.setMinutes(today.getMinutes() + 15);
            $cookies.remove("sessionMedStudio");
            $cookies.put("sessionMedStudio", sessionId, {'expires' : expiresValue});
        };

        this.destroy = function () {
            this.id = null;
            this.userId = null;
            this.userRole = null;

            $cookies.remove("sessionMedStudio");
        };
    });