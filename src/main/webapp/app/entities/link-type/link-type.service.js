(function() {
    'use strict';
    angular
        .module('myappApp')
        .factory('LinkType', LinkType);

    LinkType.$inject = ['$resource'];

    function LinkType ($resource) {
        var resourceUrl =  'api/link-types/:id';

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
