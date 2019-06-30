import Vue from 'vue';
import Router from 'vue-router';
import CompetitionsOverview from './views/CompetitionsOverview.vue';
import ChallengesOverview from './views/ChallengesOverview.vue';
import CompetitionDetail from './views/CompetitionDetail.vue';
import CreateChallenge from './views/CreateChallenge.vue';
import CreateCompetition from './views/CreateCompetition.vue';
import CompleteChallenge from './views/CompleteChallenge.vue';
import CompetitionChallenges from './views/CompetitionChallenges.vue';
import FourOhFour from '@/views/FourOhFour.vue';

Vue.use(Router);

export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      name: 'challenges',
      path: '/challenges',
      component: ChallengesOverview,
    },
    {
      name: 'CreateChallenge',
      path: '/challenges/create',
      component: CreateChallenge,
    },
    {
      name: 'competitions',
      path: '/competitions',
      component: CompetitionsOverview,
    },
    {
      name: 'CreateCompetition',
      path: '/competitions/create',
      component: CreateCompetition,
    },
    {
      name: 'competitionDetail',
      path: '/competitions/:competitionId',
      component: CompetitionDetail,
      props: true,
    },
    {
      name: 'competitionChallenges',
      path: '/competitions/:competitionId/complete',
      component: CompetitionChallenges,
      props: true,
    },{
      name: 'completeChallenge',
      path: '/competitions/:competitionId/complete/:challengeId',
      component: CompleteChallenge,
      props: true,
    },
    {
      name: 'about',
      path: '/about',
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import(/* webpackChunkName: "about" */ './views/About.vue'),
    },
    {
      name: '404',
      path: '/404',
      component: FourOhFour,
    },
    {
      path: '*',
      redirect: '/404',
    },
  ],
});
