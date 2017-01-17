/**
 * Created by Savek on 2017-01-14.
 */
angular
    .module('util', ['ngCookies'])
    .factory('AuthService', function ($http, $location, $cookies, Session) {
        var authService = {};

        authService.login = function (credentials) {

            var headers = credentials ? {authorization : "Basic "
                + btoa(credentials.username + ":" + credentials.password )
                } : {};

            return $http
                .get('/user', {headers : headers})
                .then(function (res) {
                    if (res.data.authenticated) {
                        Session.create(res.data.details.sessionId, res.data.name,
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

        authService.isAuthorized = function (authorizedRoles) {
            if (!angular.isArray(authorizedRoles)) {
                authorizedRoles = [authorizedRoles];
            }
            return (authService.isAuthenticated() &&
            authorizedRoles.indexOf(Session.userRole) !== -1);
        };

        authService.logout = function () {
            return $http.post('/logoutUser', {}).then(function () {
                Session.destroy();
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
            $cookies.put("sessionMedStudio", sessionId, {'expires' : expiresValue});
        };

        this.destroy = function () {
            this.id = null;
            this.userId = null;
            this.userRole = null;
            $cookies.remove("sessionMedStudio");
        };
    });