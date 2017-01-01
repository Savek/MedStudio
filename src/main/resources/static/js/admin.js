
angular
    .module('admin', [ 'ngRoute', 'ngAnimate', 'ngTouch', 'ui.bootstrap'])
    .config(
        function($routeProvider, $httpProvider) {

            $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

            $routeProvider.when('/userDetails', {
                templateUrl: 'userDetails.html',
            }).otherwise('/');
        }
    )
    .controller('adminController',
        function($scope, $http, $location) {

            $scope.actualPage = $location.path()
                .replace("\/", "")
                .replace(/([a-z]*)([A-Z]?.*)/, "$1 $2");

            $scope.update = function(updateUser) {

                $http
                    .post('/updateUser', updateUser)
                    .then(function(response) {
                        $scope.user = angular.copy($scope.userUpdate);
                    }, function() {

                    });
            };

            $scope.logout = function() {
                $http.post('/logoutUser', {}).finally(function () {
                    $rootScope.authenticated = false;
                    $rootScope.headers = {};
                    $location.path("/");
                });
            }

            $http.post('/user').then(function(response) {
                if (response.data.enabled == true) {
                    $scope.authenticated = true;
                    $scope.user = response.data;
                    $scope.userUpdate = angular.copy(response.data);
                } else {
                    $scope.authenticated = false;
                    $location.path("/");
                }

            }, function() {
                $scope.authenticated = false;
                $location.path("/");
            });

            if ($scope.authenticated == false) {
                $location.path("/");
            } else {
                $scope.showUser = true;
            }
        }
    );