/**
 * Created by Savek on 2017-01-12.
 */
angular.module('authModule', [])
    .factory('authServiceFactory', function ($http, $location, Session) {
        var authService = {};

        authService.login = function (credentials) {
            return $http
                .post('/login', credentials)
                .then(function (res) {
                    $location.path("/");
                    Session.create(res.data.id, res.data.user.id,
                        res.data.user.role);
                    return res.data.user;
                });
        };

        authService.isAuthenticated = function () {
            return !!Session.userId;
        };

        authService.isAuthorized = function (authorizedRoles) {
            if (!angular.isArray(authorizedRoles)) {
                authorizedRoles = [authorizedRoles];
            }
            return (authService.isAuthenticated() &&
            authorizedRoles.indexOf(Session.userRole) !== -1);
        };

        return authService;
    }).service('sessionService', function () {
    this.create = function (sessionId, userId, userRole) {
        this.id = sessionId;
        this.userId = userId;
        this.userRole = userRole;
    };
    this.destroy = function () {
        this.id = null;
        this.userId = null;
        this.userRole = null;
    };
})