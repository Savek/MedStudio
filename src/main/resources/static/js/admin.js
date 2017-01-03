
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

            $scope.$on('$locationChangeStart', function(event) {
                $scope.userUpdateSucess = null;

                $scope.actualPage = $location.path()
                    .replace("\/", "")
                    .replace(/([a-z]*)([A-Z]?.*)/, "$1 $2");
            });

            $scope.userUpdateSucess = null
            $scope.update = function(updateUser) {

                $http
                    .post('/updateUser', updateUser)
                    .then(function(response) {
                        $scope.user = angular.copy($scope.userUpdate);
                        $scope.userUpdateSucess = true;
                    }, function() {
                        $scope.userUpdateSucess = false;
                    });
            };

            $scope.logout = function() {

                $http.post('/logoutUser', {}).finally(function () {
                    $rootScope.authenticated = false;
                    $rootScope.headers = {};
                    $location.path("/");
                });
            };

            $scope.previewFile = function() {

                var file = document.querySelector('#file').files[0];
                var reader = new FileReader();
                reader.onload = function() {
                    $scope.userUpdate.image = reader.result
                                                        .replace("data:image\/png;base64,", "")
                                                        .replace("data:image\/jpeg;base64,", "");
                    document.querySelector('#userDetailsAvatar').src = reader.result;
                };
                reader.readAsDataURL(file);
            };

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