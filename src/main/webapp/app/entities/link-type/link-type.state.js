(function() {
    'use strict';

    angular
        .module('myappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('link-type', {
            parent: 'entity',
            url: '/link-type?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'myappApp.linkType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/link-type/link-types.html',
                    controller: 'LinkTypeController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('linkType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('link-type-detail', {
            parent: 'link-type',
            url: '/link-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'myappApp.linkType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/link-type/link-type-detail.html',
                    controller: 'LinkTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('linkType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LinkType', function($stateParams, LinkType) {
                    return LinkType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'link-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('link-type-detail.edit', {
            parent: 'link-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/link-type/link-type-dialog.html',
                    controller: 'LinkTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LinkType', function(LinkType) {
                            return LinkType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('link-type.new', {
            parent: 'link-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/link-type/link-type-dialog.html',
                    controller: 'LinkTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                logoFilepath: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('link-type', null, { reload: 'link-type' });
                }, function() {
                    $state.go('link-type');
                });
            }]
        })
        .state('link-type.edit', {
            parent: 'link-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/link-type/link-type-dialog.html',
                    controller: 'LinkTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LinkType', function(LinkType) {
                            return LinkType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('link-type', null, { reload: 'link-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('link-type.delete', {
            parent: 'link-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/link-type/link-type-delete-dialog.html',
                    controller: 'LinkTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LinkType', function(LinkType) {
                            return LinkType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('link-type', null, { reload: 'link-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
