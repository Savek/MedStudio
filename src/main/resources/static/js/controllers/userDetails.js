/**
 * Created by Savek on 2017-01-15.
 */
angular
    .module('admin')
    .controller('userDetailsController', function ($scope, $http, $routeParams) {

        $scope.$on('$locationChangeStart', function () {
            $scope.userUpdateSucess = null;
        });

        $scope.myProfile = angular.isUndefined($routeParams.userId);
        var restLink = angular.isUndefined($routeParams.userId)?"/userInfo":"/userInfo/"+$routeParams.userId;
        $http.get(restLink).then(function (response) {
            if (response.data.enabled == true) {
                $scope.authenticated = true;
                $scope.user = response.data;
                $scope.userUpdate = angular.copy(response.data);
            }
        });

        $scope.userUpdateSucess = null;

        $scope.update = function (updateUser) {

            $http
                .post('/updateUser', updateUser)
                .then(function () {
                    $scope.user = angular.copy($scope.userUpdate);
                    $scope.userUpdateSucess = true;
                }, function () {
                    $scope.userUpdateSucess = false;
                });
        };

        $scope.previewFile = function () {

            var file = document.querySelector('#file').files[0];
            var reader = new FileReader();
            reader.onload = function () {
                $scope.userUpdate.image = reader.result
                    .replace("data:image\/png;base64,", "")
                    .replace("data:image\/jpeg;base64,", "");
                document.querySelector('#userDetailsAvatar').src = reader.result;
            };
            reader.readAsDataURL(file);
        };

    });