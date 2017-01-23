angular
    .module('admin')
    .controller('hospitalController', function ($scope, $http) {

        $scope.addHospital = function() {

            var data = $scope.newHospital;

            $http.post('/addHospitalToDB', data).then(function () {
                $scope.succes = true;
            }, function () {
                $scope.succes = false;
            });
        };
    });