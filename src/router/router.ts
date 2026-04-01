import { Router } from "vue-router"

let routerInstance: Router

export const setRouter = (r: Router) => {
  routerInstance = r
}

export const getRouter = () => routerInstance
