(function() {
    'use strict';
    angular
        .module('myappApp')
        .factory('Link', Link);

    Link.$inject = ['$resource'];

    function Link ($resource) {
        var resourceUrl =  'api/links/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
