export class Usuario {
    private userId:number = -1;
    private username:string = "";
    private role:string = "";
    private email:string = "";
    private userToken:string = "";

    constructor(userId:number, username:string, role : string, email: string) {
      this.userId = userId;
      this.username = username;
      this.role = role;
      this.email = email;
    }

    getUserId(): number{
      return this.userId;
    }

    getUsername(): string{
      return this.username;
    }

    getRole(): string{
      return this.role;
    }

    getEmail(): string{
        return this.email;
      }

      setUserToken(newToken:string){
        this.userToken = newToken;
      }
  }