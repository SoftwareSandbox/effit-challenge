import Vue from 'vue';
import Router from 'vue-router';
import ChallengesOverview from './components/ChallengesOverview.vue';

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
      name: 'about',
      path: '/about',
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import(/* webpackChunkName: "about" */ './views/About.vue'),
    },
  ],
});
