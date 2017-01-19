
angular
    .module('admin')
    .controller('addUserPanelController', function ($scope, $http, $routeParams) {


        $http.get('/getHospitals').then(function (response) {
            $scope.hospitals = response.data;
        });

        $http.get('/getRoles').then(function (response) {
            $scope.roles = response.data;
        });

        $scope.succes = null;

        $scope.addNewUser = function() {

            var data = $scope.newUser;

            $http.post('/addUserToDB', data).then(function () {
                $scope.succes = true;
            }, function () {
                $scope.succes = false;
            });
        };



    });