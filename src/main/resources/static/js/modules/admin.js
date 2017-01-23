angular
    .module('admin', ['ngRoute', 'ngAnimate', 'ngTouch', 'ui.bootstrap', 'chart.js', 'util'])
    .constant('RESULT_TYPES', {
        pressure: 0,
        temperature: 1,
        pulse: 2
    })
    .config(
        function ($routeProvider, $httpProvider) {

            $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

            $routeProvider.when('/userDetails/:userId?', {
                templateUrl: 'userDetails.html'
            }).when('/results/:userId/:resultType', {
                templateUrl: 'result.html'
            }).when('/resultDetails/:userId/:resultType/:date', {
                templateUrl: 'resultsDetails.html'
            }).when('/doctorPanel/:userId', {
                templateUrl: 'doctorPanel.html'
            }).when('/usersPanel', {
                templateUrl: 'viewUsers.html'
            }).when('/addUser', {
                templateUrl: 'addUser.html'
            }).when('/addHospital', {
                templateUrl: 'addHospital.html'
            }).when('/hospitalsPanel', {
                templateUrl: 'viewHospitals.html'
            }).otherwise('/');
        }
    )
    .controller('adminController',
        function ($rootScope, $scope, $http, $location, $window, AuthService, RESULT_TYPES) {

            $scope.RESULT_TYPES = RESULT_TYPES;

            $scope.backArrow = function(){
                window.history.back();
            };

            $scope.logout = function () {
                AuthService.logout();
                $window.location.href = '/index.html';
            };

            $http.get('/userInfo').then(function (response) {
                if (response.data.enabled == true) {
                    $scope.authenticated = true;
                    $scope.user = response.data;

                    $scope.isAdmin = false;
                    $scope.isDoctor = false;
                    $scope.isPatient = false;
                    if ($scope.user.role.role === "ROLE_ADMIN") {
                        $scope.isAdmin = true;
                    }
                    if ($scope.user.role.role === "ROLE_DOC") {
                        $scope.isDoctor = true;
                    }
                    if ($scope.user.role.role === "ROLE_PATIENT") {
                        $scope.isPatient = true;
                    }

                }
            }, function () {
                $location.path("/");
            });

            if (AuthService.isAuthenticated() == false) {
                $location.path("/");
            }

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

            AuthService.login();
        }
    );