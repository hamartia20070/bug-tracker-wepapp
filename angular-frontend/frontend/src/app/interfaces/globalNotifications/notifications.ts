export interface Alert {
    type: string;
    message: string;
  }

export const alerts: Alert[] = [];

  export function addNewAlert(message:string,type:string){
      let newAlert: Alert = {type:type,message:message};
      alerts.push(newAlert);
  }