(function() {
    'use strict';
    angular
        .module('myappApp')
        .factory('Provider', Provider);

    Provider.$inject = ['$resource'];

    function Provider ($resource) {
        var resourceUrl =  'api/providers/:id';

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
