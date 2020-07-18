const mutations = {
  LOCK_SCREEN(state){
    state.lockScreen = true
  },
  UNLOCK_SCREEN(state){
    state.lockScreen = false
  }
};
export default mutations