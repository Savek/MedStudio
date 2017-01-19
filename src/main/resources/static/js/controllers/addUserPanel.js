
angular
    .module('admin')
    .controller('addUserPanelController', function ($scope, $http, $routeParams) {


        $http.get('/getHospitals').then(function (response) {
            $scope.hospitals = response.data;
        });

        $http.get('/getRoles').then(function (response) {
            $scope.roles = response.data;
        });

        $scope.addNewUser = function() {
            var data=$scope.fields;

            $http.get('/addUser', data).then(function (response) {

            }, function () {

            });
        };



    });