/**
 * Created by Savek on 2017-01-21.
 */
angular
    .module('admin')
    .controller('viewHospitalsController', function ($scope, $http) {

        $scope.hospitalsSliced = [];
        $scope.hospitalssAll = [];

        $scope.getHospitals = function() {

            $http.get('/getHospitals').then(function (response) {

                $scope.hospitalsAll = response.data;

                $scope.totalItems = response.data.length;
                $scope.currentPage = 1;
                $scope.hospitalsSliced = $scope.hospitalsAll.slice(0, 10);
            });
        };

        $scope.getHospitals();

        $scope.pageChanged = function() {
            $scope.hospitalsSliced = $scope.hospitalsAll.slice(($scope.currentPage - 1) * 10, $scope.currentPage * 10);
        };

        $scope.deleteHospital = function(hospitalId) {
            $http.delete('/removeHospital/' + hospitalId).then(function() {

                $scope.getHospitals();
            });
        };
    });