angular.module('main', [ 'ngRoute', 'ngAnimate', 'ngTouch', 'ui.bootstrap'])
    .config(function($routeProvider, $httpProvider) {

        $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

        $routeProvider.when('/', {
            templateUrl : 'home.html',
            controller : 'navigation',
            controllerAs: 'controller'
        }).when('/login', {
            templateUrl : 'login.html',
            controller : 'navigation',
            controllerAs: 'controller'
        }).when('/admin', {
            templateUrl : 'admin.html',
            controller : 'navigation',
            controllerAs: 'controller'
        }).otherwise('/');

    }).controller('navigation',
    function($rootScope, $http, $location) {

        $rootScope.isNavCollapsed = true;

        var self = this;

        var authenticate = function(credentials, callback) {

            var headers = credentials ? {authorization : "Basic "
            + btoa(credentials.username + ":" + credentials.password )
            } : {};

            $http.get('user', {headers : headers}).then(function(response) {
                if (response.data.name) {
                    $rootScope.authenticated = true;
                    $location.path("/");
                } else {
                    $rootScope.authenticated = false;
                }
                callback && callback();
            }, function() {
                $rootScope.authenticated = false;
                callback && callback();
            });

        }

        self.credentials = {};
        self.login = function() {
            authenticate(self.credentials, function() {
                if ($rootScope.authenticated) {
                    $location.path("/");
                    self.error = false;
                } else {
                    $location.path("/login");
                    self.error = true;
                }
            });
        };
        self.logout = function() {
            $http.post('/logout', {}).finally(function () {
                $rootScope.authenticated = false;
                $rootScope.headers = {};
                $location.path("/");
            });
        }
    }
    ).controller('CarouselDemoCtrl', function ($scope) {
        $scope.myInterval = 3000;
        $scope.noWrapSlides = false;
        $scope.active = 0;
        var slides = $scope.slides = [];
        var currIndex = 0;

        $scope.addSlide = function() {
            var newWidth = 1900 + slides.length + 1;
            slides.push({
                image: '//unsplash.it/' + newWidth + '/600',
                text: ['Lorem ipsum','Lorem ipsum','Lorem ipsum','Lorem ipsum'][slides.length % 4],
                id: currIndex++
            });
        };

        $scope.randomize = function() {
            var indexes = generateIndexesArray();
            assignNewIndexesToSlides(indexes);
        };

        for (var i = 0; i < 4; i++) {
            $scope.addSlide();
        }

        // Randomize logic below

        function assignNewIndexesToSlides(indexes) {
            for (var i = 0, l = slides.length; i < l; i++) {
                slides[i].id = indexes.pop();
            }
        }

        function generateIndexesArray() {
            var indexes = [];
            for (var i = 0; i < currIndex; ++i) {
                indexes[i] = i;
            }
            return shuffle(indexes);
        }
    });
