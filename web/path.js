function getURL(action) {
//   var host = "https://forvoid.me:8443/RTC_Demo/";
  var host = "http://192.168.10.105:8080/";

//   var ws =  'wss://forvoid.me:8443/RTC_Demo/';
  var ws =  'ws://192.168.10.105:8080/';
    var _maps = {
       
        regist: "userinfo!regist.action", //username password real name addr setmealid
        login: 'userinfo!login.action', // username password
        logout:'userinfo!destroy.action',

        updateUserInfo: 'userinfo!updateUserInfo.action', //realname addr
        seeUserInfo: 'userinfo!seeUserInfo.action', //id
        updateUserPassword: 'userinfo!updateUserPassword.action', //password

        createGroup: 'group!createGroup.action', //name
        findGroupByName: 'group!findByName.action', //name
        joinGroup: 'group!addGroup.action', //gid
        groupMembers: 'group!findUserinfoByGid.action', //gid
        findGroupByUid: 'group!findGroupByUid.action', //uid


        charge: 'bill!addRecharge.action', //uid createid money
        queary: 'bill!billSumById.action', //uid



        callRecord: 'bill!findRecordByUid.action', //uid page row
        chargeRecord: 'bill!findRechargeByUid.action', //uid page row


        //套餐
        getAllOption:'setmeal!findALl.action',
        optionDetail:'setmeal!findById.action',//id

    }

    if(action=='ws')return ws;
    if (_maps[action]) {
        return host + _maps[action];
    } else {
        return '/';
    }

}
