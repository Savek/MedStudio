
angular
    .module('main')
    .controller('adminController',
        function($scope, $http, $location) {
            $http.post('/user').then(function(response) {
                if (response.data.enabled == true) {
                    $scope.authenticated = true;
                    $scope.userName = response.data.name;
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