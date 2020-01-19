import Vue from 'vue';
import Router from 'vue-router';
import CompetitionsOverview from './views/CompetitionsOverview.vue';
import CompetitionDetail from './views/CompetitionDetail.vue';
import CreateCompetition from './views/CreateCompetition.vue';
import CompleteChallenge from './views/CompleteChallenge.vue';
import CompetitionChallenges from './views/CompetitionChallenges.vue';
import FourOhFour from '@/views/FourOhFour.vue';
import About from '@/views/About.vue';

Vue.use(Router);

export default new Router({
  routes: [
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
      name: 'HostAgain',
      path: '/competitions/hostagain/:competitionId',
      component: CreateCompetition,
      props: true,
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
    },
    {
      name: 'completeChallenge',
      path: '/competitions/:competitionId/complete/:challengeId',
      component: CompleteChallenge,
      props: true,
    },
    {
      name: 'about',
      path: '/about',
      component: About,
    },
    {
      name: '404',
      path: '/404',
      component: FourOhFour,
    },
    {
      path: '*',
      redirect: '/about',
    },
  ],
});
