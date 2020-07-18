import Vue from 'vue';
import VueRouter from 'vue-router';
import TokenService from "@/services/TokenService";
import RefreshTokenService from "@/services/RefreshTokenService";
Vue.use(VueRouter);

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: '/',
      component: ()=>import('@/views/Index')
    },
    {
      path: '/login',
      name: 'login',
      component: ()=>import('@/views/login/Index'),
      meta: {
        public: true,
        onlyWhenLoggedOut: true
      }
    },
    {
      path: '/master',
      name: 'master',
      component: ()=>import('@/views/master/Index'),
      meta:{
        onlyForAdmin: true
      },
      children:[
        {
          path: 'menu',
          name:'masterMenu',
          component: ()=>import('@/views/master/Menu')
        },
        {
          path: 'employee',
          name:'masterEmployee',
          component: ()=>import('@/views/master/Employees')
        },
        {
          path: '/master/product',
          name:'masterProduct',
          component: ()=>import('@/views/master/Products')
        },
        {
          path: '/master/store',
          component: ()=>import('@/views/master/Stores')
        },
        {
          path: '/master/supplier',
          component: ()=>import('@/views/master/Suppliers')
        },
        {
          path: '/master/approvalflow',
          component: ()=>import('@/views/master/ApprovalFlow')
        },
        {
          path: '/master/charge',
          component: ()=>import('@/views/master/Charge')
        },
        {
          path: '/master/imported-situation',
          component: ()=>import('@/views/master/ImportedSituation')
        },
      ]
    },
    {
      path: '/request',
      name: 'request',
      component: ()=>import('@/views/requests/Index'),
      children: [
        {
          path: 'list',
          name: 'request-list',
          component: () => import('@/views/requests/RequestList'),
        },
        {
          path: 'create',
          component: ()=>import('@/views/requests/Create'),
        },
        {
          path: '/request/confirm',
          component: ()=>import('@/views/requests/Confirm'),
        },
        {
          path: '/request/apply',
          component: ()=>import('@/views/requests/Apply'),
        },
        {
          path: '/request/approve',
          component: ()=>import('@/views/requests/Approve'),
        },
        {
          path: 'confirm-settlement',
          component: ()=>import('@/views/requests/ConfirmSettlement'),
        }
      ]
    },
    {
      path: '/account-receivables',
      name: 'account-receivables',
      component: ()=>import('@/views/account_receivables/Index'),
      children: [
        {
          path: 'create',
          component: ()=>import('@/views/account_receivables/Create'),
        },
        {
          path: '/account-receivables/create/:cd/:mode?',
          component: ()=>import('@/views/account_receivables/Create'),
        },
        {
          path: 'confirm-create/:cd/:mode?',
          component: ()=>import('@/views/account_receivables/ConfirmCreate'),
        },
        {
          path: 'apply/:cd/:mode?',
          component: ()=>import('@/views/account_receivables/Apply'),
        },
        {
          path: 'approve/:cd/:mode?',
          component: ()=>import('@/views/account_receivables/Approve'),
        },
        {
          path: 'reject/:cd/:mode?',
          component: ()=>import('@/views/account_receivables/Approve'),
        },
        {
          path: 'confirm-settlement/:cd/:mode?',
          component: ()=>import('@/views/account_receivables/ConfirmSettlement'),
        },
        {
          path: 'payment-done/:cd/:mode?',
          component: ()=>import('@/views/account_receivables/PaymentDone.vue'),
        },
        {
          path: 'tablet/approve/:cd/:mode?',
          component: ()=>import('@/views/account_receivables/IndexTablet.vue'),
        }
      ]
    },
    {
      path: '/exhibition-promotions',
      name: 'exhibition-promotions',
      component: ()=>import('@/views/exhibition_promotions/Index'),
      children: [
        {
          path: 'create',
          component: ()=>import('@/views/exhibition_promotions/Create'),
        },
        {
          path: 'create/:cd',
          component: ()=>import('@/views/exhibition_promotions/Create'),
        },
        {
          path: 'approve/:cd?/:mode?',
          component: ()=>import('@/views/exhibition_promotions/Approve'),
        },
        {
          path: 'tablet/approve/:cd?/:mode?',
          component: ()=>import('@/views/mannequin_promotions/IndexTablet'),
        }
      ]
    },
    {
      path: '/mannequin-promotions',
      name: 'mannequin-promotions',
      component: ()=>import('@/views/mannequin_promotions/Index'),
      children: [
        {
          path: 'create',
          component: ()=>import('@/views/mannequin_promotions/Create'),
        },
        {
          path: '/mannequin-promotions/create/:cd/:mode?',
          component: ()=>import('@/views/mannequin_promotions/Create'),
        },
        {
          path: 'apply/:cd/:mode?',
          component: ()=>import('@/views/mannequin_promotions/Apply'),
        },
        {
          path: 'create/:cd',
          component: ()=>import('@/views/mannequin_promotions/Create'),
        },
        {
          path: 'approve/:cd/:mode?',
          component: ()=>import('@/views/mannequin_promotions/Approve'),
        },
        {
          path: 'tablet/approve/:cd/:mode?',
          component: ()=>import('@/views/mannequin_promotions/IndexTablet'),
        },
        {
          path: 'reject/:cd/:mode?',
          component: ()=>import('@/views/mannequin_promotions/Approve'),
        },
        {
          path: 'confirm-settlement/:cd/:mode?',
          component: ()=>import('@/views/mannequin_promotions/ConfirmSettlement'),
        },
        {
          path: 'payment-done/:cd/:mode?',
          component: ()=>import('@/views/mannequin_promotions/PaymentDone.vue'),
        }
      ]
    },
    {
      path: '/user',
      name: 'user',
      component: ()=>import('@/views/user/Index'),
      children: [
        {
          path: 'edit',
          name: 'user-edit',
          component: () => import('@/views/user/Edit'),
        }
      ]
    },
    {
      path: "/files",
      component: () => import('@/views/Download'),
      meta: {
        showLayout: false
      }
    },
    {
      path: '*',
      redirect: '/'
    }
  ],
});
router.beforeEach(async (to, from, next) => {
  const isPublic = to.matched.some(record => record.meta.public);
  const isLoginPage = from.name == "login";
  const onlyWhenLoggedOut = to.matched.some(
    record => record.meta.onlyWhenLoggedOut
  );
  const loggedIn = TokenService.getToken() ;
  const refreshToken = TokenService.getRefreshToken();
  if (!isPublic && !loggedIn) {
    if( !isLoginPage) {
      if(refreshToken) {
        RefreshTokenService.callRefreshToken()
        .then(response => {
          if(response) {
            next(to);
          }
        })
        .catch(err => {
          next({
            path: "/login",
            query: { redirect: to.fullPath }
          })
        });
      } else {
        return next({
          path: "/login",
          query: { redirect: to.fullPath }
        });
      }
    }
  } else if (loggedIn && onlyWhenLoggedOut) {
    // Do not allow user to visit login page or register page if they are logged in
    return next("/");
  } else {
    next();
  }
})
export default router;