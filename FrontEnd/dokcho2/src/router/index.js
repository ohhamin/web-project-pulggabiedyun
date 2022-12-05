import { createRouter, createWebHistory } from 'vue-router'
import { BASE_URL } from '@/constant/BASE_URL'
import axios from 'axios'
import StartView from '../views/start/StartView'

// webpackPrefetch:true 가 추가되어있으면 제일 처음에 데이터 받아옴
// 빈도가 많거나 사이즈가 크면 추가

const isAccessTokenExpired = function isAccessTokenExpired() {
  let expire = false
  // accessToken에서 .(도트)로 분리하여 payload를 가져옵니다.
  if (localStorage.getItem('accessToken') !== null) {
    let base64Payload = localStorage.getItem('accessToken').split('.')[1]
    // URL과 호환되지 않는 문자를 base64 표준 문자로 교체합니다.
    base64Payload = base64Payload.replace(/-/g, '+').replace(/_/g, '/')
    // atob() 메소드로 복호화합니다.
    base64Payload = atob(base64Payload)
    // JSON 객체로 변환합니다.
    const payloadObject = JSON.parse(base64Payload)
    // accessToken의 만료 시간을 확인합니다.
    const currentDate = new Date().getTime() / 1000
    if (payloadObject.exp <= currentDate) {
      expire = true
    }
  }
  return expire
}

const doRefreshToken = async function doRefreshToken() {
  if (localStorage.getItem('accessToken') !== '') {
    const token = {
      accessToken: localStorage.getItem('accessToken'),
      refreshToken: localStorage.getItem('refreshToken')
    }
    try {
      const result = await axios.post(
        BASE_URL + '/api/v1/user/auth/refresh',
        token
      )
      if (result.status === 200) {
        localStorage.setItem('accessToken', result.data.accessToken)
        localStorage.setItem('refreshToken', result.data.refreshToken)
        axios.defaults.headers.common.AUTHORIZATION = result.data.accessToken
        location.reload()
      } else {
        localStorage.clear()
        location.reload()
      }
    } catch (err) {
      console.log(err)
      localStorage.clear()
      location.reload()
    }
  } else {
    localStorage.clear()
    location.reload()
  }
}

const routes = [
  // 시작 (로그인 페이지)
  {
    path: '/',
    name: 'start',
    component: StartView
  },
  {
    path: '/e',
    name: 'ending',
    component: () =>
      import(
        /* webpackChunkName: "findpassword" */ '../components/ending/EndingCredits.vue'
      )
  },
  // 비밀번호 찾기
  {
    path: '/findpassword',
    name: 'findpassword',
    component: () =>
      import(
        /* webpackChunkName: "findpassword" */ '../views/start/FindPasswordView.vue'
      )
  },
  // 회원가입
  {
    path: '/signup',
    name: 'signup',
    component: () =>
      import(
        /* webpackChunkName: "signup", webpackPrefetch:true */ '../views/start/SignUpView.vue'
      )
  },
  // 닉네임지정
  {
    path: '/set/nickname',
    name: 'setnickname',
    component: () =>
      import(
        /* webpackChunkName: "intro", webpackPrefetch:true */ '../views/start/SetNicknameView.vue'
      )
  },
  // 스타팅풀깨비
  {
    path: '/startingcard',
    name: 'startingcard',
    component: () =>
      import(
        /* webpackChunkName: "intro", webpackPrefetch:true */ '../views/start/StartingCardView.vue'
      )
  },
  // 메인페이지
  {
    path: '/main',
    name: 'main',
    component: () =>
      import(
        /* webpackChunkName: "main", webpackPrefetch:true */ '../views/canvas/CanvasView.vue'
      )
  }, // 404 에러
  {
    path: '/404',
    name: 'notFound',
    component: () =>
      import(/* webpackChunkName: "404" */ '../views/error/NotFound404.vue')
  },
  // 404로 보냄
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach(async (to, from, next) => {
  let token = ''
  if (localStorage.getItem('accessToken')) {
    token = localStorage.getItem('accessToken')
  }
  if (
    to.path === '/' ||
    to.path === '/signup' ||
    to.path === '/findpassword' ||
    to.path === '/startingcard' ||
    to.path === '/set/nickname' ||
    to.path === '/404'
  ) {
    const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    if (localStorage.getItem('accessToken') && userInfo.nickname !== '') {
      return router.push({ path: '/main' })
    }
    next()
  } else if (token) {
    if (!isAccessTokenExpired()) {
      return next()
    } else {
      doRefreshToken()
    }
  } else {
    return next({ path: '/' })
  }
})

export default router
