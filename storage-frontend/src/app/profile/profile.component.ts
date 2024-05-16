import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { TokenService } from '../_services/token.service';
import { Usuario } from '../_interfaces/user.type';
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  currentUser: Usuario | null = null;

  constructor(private userService: UserService,private tokenService: TokenService) { }

  ngOnInit(): void {
    this.currentUser = this.userService.getUser(this.tokenService.getToken());
  }
}
