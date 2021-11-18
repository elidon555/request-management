import { Component, OnInit } from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, NgControl, Validators} from "@angular/forms";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Router} from "@angular/router";


@Component({
  selector: 'app-create-list',
  templateUrl: './create-list.component.html',
  styleUrls: ['./create-list.component.css']
})
export class CreateListComponent{

  data = {

    resourceDTOS: [
      {
        seniority: "",
        skillDTOS: [
          {
            skill: "",
          }
        ],
        resourceNotes:""
      }
    ]
  }

  myForm: FormGroup;

  isButtonEnabled=true;


  constructor(private fb: FormBuilder,private http:HttpClient,private router:Router) {
    this.myForm = this.fb.group({
      areaOfInterest: new FormControl("",Validators.required),
      startDate:new FormControl(null, Validators.required),
      endDate:new FormControl(null, Validators.required),
      description:new FormControl(null, Validators.required),
      notes:new FormControl(null, Validators.required),
      createdBy:[localStorage.getItem("currentEmail")],
      resourceDTOS: this.fb.array([])
    })

    this.data = {


      resourceDTOS: [
        {
          seniority: "",
          skillDTOS: [
            {
              skill: "",
            }
          ],
          resourceNotes:""
        }
      ]
    }
    this.setResourceDTOS()


  }

  enableButton(){
    this.isButtonEnabled=true;
  }
  disableButton(){
    this.isButtonEnabled=false;
  }


  onSubmit(formData) {

   this.disableButton()

    const httpOptions = {
      headers: new HttpHeaders({
        "Authorization": "Token " + localStorage.getItem("token")
      })
    };

    console.log(formData)
    document.getElementById("deleteButton2").click();
    this.http.post('http://localhost:8080/api/request', formData,httpOptions).subscribe(data=>{
      console.warn("data",data)
      this.router.navigate(['/request-list'])
    },error => {

      document.getElementById("deleteButton1").click();
      this.enableButton()
    });
    this.enableButton()

  }

  get areaOfInterest() {
    return this.myForm.get('areaOfInterest');
  }
  get startDate() {
    return this.myForm.get('startDate');
  }
  get endDate() {
    return this.myForm.get('endDate');
  }
  get description() {
    return this.myForm.get('description');
  }
  get notes() {
    return this.myForm.get('notes');
  }


  addNewResourceDTOS() {
    let control = <FormArray>this.myForm.controls.resourceDTOS;
    control.push(
        this.fb.group({
          seniority: new FormControl(null, Validators.required),
          skillDTOS: this.fb.array([this.fb.group({
            skill: new FormControl('',Validators.required)
          })]),
          resourceNotes: new FormControl(null, Validators.required),
        })
    );
  }

  deleteResourceDTOS(index) {
    let control = <FormArray>this.myForm.controls.resourceDTOS;
    control.removeAt(index)
  }

  addNewResourceSkill(control) {
    control.push(
        this.fb.group({
          skill: new FormControl("",Validators.required),
        }))

  }

  deleteResourceSkill(control, index) {
    control.removeAt(index)
  }

  setResourceDTOS() {
    let control = <FormArray>this.myForm.controls.resourceDTOS;
    this.data.resourceDTOS.forEach(x => {
      control.push(this.fb.group({
        seniority: x.seniority,
        skillDTOS: this.setResourceSkills(x),
        resourceNotes:x.resourceNotes }))
    })
  }

  setResourceSkills(x) {
    let arr = new FormArray([])
    x.skillDTOS.forEach(y => {
      arr.push(this.fb.group({
        skill: new FormControl(y.skill, Validators.required),
      }))
    })
    return arr;
  }



}
