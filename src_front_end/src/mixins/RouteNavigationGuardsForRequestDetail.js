import TokenService from "@/services/TokenService";
export default {
  beforeRouteEnter(to, from, next) {
    console.log("Enter")
    if(to.params.cd && ((from.params.cd && from.params.cd != to.params.cd) || !from.params.cd)) {
      // resest role user for request
      TokenService.resetRolesForRequest();
    }
    if(to.params.mode == "edit" && !to.path.includes("/create")) {
      to.params.mode = "view";
      let paths = to.path.split("/");
      let newPath = to.path;
      if(paths[paths.length - 1] == "edit") {
        paths[paths.length - 1] = "view";
        newPath = paths.join("/");
      }
      next({path: newPath, params: {cd: to.params.cd, mode: "view"}});
    } else {
      next();
    }
  },
  beforeRouteUpdate(to, from, next) {
    console.log("Update")
    if(to.params.cd && ((from.params.cd && from.params.cd != to.params.cd) || !from.params.cd)) {
        // resest role user for request
        TokenService.resetRolesForRequest();
      }
      if(from.params.mode == "edit") {
        this.unlockRequest(next)
        .then(isUnlocked => {
          if(isUnlocked) {
            next();
          } else {
            next(false);
          }
        })
        .catch(err => {
          next(false);
        });
      } else {
        next();
      }
  },
  beforeRouteLeave(to, from, next) {
    console.log("Leave")
    if(to.params.cd && ((from.params.cd && from.params.cd != to.params.cd) || !from.params.cd)) {
        // resest role user for request
        TokenService.resetRolesForRequest();
      }
      if(from.params.mode == "edit") {
        this.unlockRequest(next)
        .then(isUnlocked => {
          if(isUnlocked) {
            next();
          } else {
            next(false);
          }
        })
        .catch(err => {
          next(false);
        });
      } else {
        next();
      }
  }
}