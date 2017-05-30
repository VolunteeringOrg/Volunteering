(function() {
    'use strict';

    angular
        .module('myappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('program', {
            parent: 'entity',
            url: '/program?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'myappApp.program.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/program/programs.html',
                    controller: 'ProgramController',
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
                    $translatePartialLoader.addPart('program');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('program-detail', {
            parent: 'program',
            url: '/program/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'myappApp.program.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/program/program-detail.html',
                    controller: 'ProgramDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('program');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Program', function($stateParams, Program) {
                    return Program.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'program',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('program-detail.edit', {
            parent: 'program-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/program/program-dialog.html',
                    controller: 'ProgramDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Program', function(Program) {
                            return Program.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('program.new', {
            parent: 'program',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/program/program-dialog.html',
                    controller: 'ProgramDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                providerId: null,
                                name: null,
                                highlight: null,
                                dateTo: null,
                                dateFrom: null,
                                statusTypeId: null,
                                shareProgram: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('program', null, { reload: 'program' });
                }, function() {
                    $state.go('program');
                });
            }]
        })
        .state('program.edit', {
            parent: 'program',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/program/program-dialog.html',
                    controller: 'ProgramDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Program', function(Program) {
                            return Program.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('program', null, { reload: 'program' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('program.delete', {
            parent: 'program',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/program/program-delete-dialog.html',
                    controller: 'ProgramDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Program', function(Program) {
                            return Program.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('program', null, { reload: 'program' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
