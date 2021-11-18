import { Component, OnInit } from "@angular/core";
import { AuthService } from "../_services/auth.service";
import { TokenStorageService } from "../_services/token-storage.service";

@Component({
    selector: "app-login",
    templateUrl: "./login.component.html",
    styleUrls: ["./login.component.css"],
})
export class LoginComponent implements OnInit {
    public isChecked = true;

    form: any = {
        username: null,
        password: null
    };
    isLoggedIn = false;
    isLoginFailed = false;
    errorMessage = '';
    roles: string[] = [];

    constructor(
        private authService: AuthService,
        private tokenStorage: TokenStorageService
    ) {}

    ngOnInit(): void {
        localStorage.setItem("rememberMe", this.isChecked.toString());

        console.log(localStorage.getItem("savedUsername"));
        this.form.username = localStorage.getItem("savedUsername");



        if (this.tokenStorage.getToken()) {
            this.isLoggedIn = true;
            this.roles = this.tokenStorage.getUser().roles;
        }
    }

    saveLocalStorage() {
        let rememberMeStatus = this.isChecked;
        let status = !rememberMeStatus;
        let finalStatus = status.toString();
        localStorage.setItem("rememberMe", finalStatus);
        console.log(localStorage.getItem("rememberMe"));
    }

    onSubmit() {
        const { username, password } = this.form;

        this.authService.login(username, password).subscribe(
            (data) => {
                localStorage.setItem('jwtUsername',username);
                localStorage.setItem('jwtPassword',password);
                this.tokenStorage.saveToken(data.accessToken);
                this.tokenStorage.saveRefreshToken(data.refreshToken);
                this.tokenStorage.saveUser(data);

                this.isLoginFailed = false;
                this.isLoggedIn = true;
                this.roles = this.tokenStorage.getUser().roles;
                // this.reloadPage();
                this.redirect();
            },
            (err) => {
                this.errorMessage = err.error.message;
                this.isLoginFailed = true;
            }
        );
    }

    redirect(): void {
      location.href = "/request-list";
    }

    // reloadPage(): void {
    //     window.location.reload();
    // }
}
