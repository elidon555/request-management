import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {RequestService} from "../_services/request-service";
import {CustomeDateValidators} from "./datavalidator"
import {ActivatedRoute, Route} from "@angular/router";


@Component({
  selector: 'app-edit-list',
  templateUrl: './edit-list.component.html',
  styleUrls: ['./edit-list.component.css']
})
export class EditListComponent implements OnInit {

  datechecker:any
  resource:any;
  currentStatusValue;
  ifAdmin=localStorage.getItem('ifAdmin');
  currentStatus:any;

  currentEmail=localStorage.getItem('currentEmail')

  requestArray:any;
  control:any;
  modal3='1'
  ngOnInit(): void {
   if (localStorage.getItem('testIfTrue')=='1'){
     document.getElementById("deleteButton2").click();
     localStorage.setItem('testIfTrue','0')
   }



  }

  data = {
    areaOfInterest:"",
    notes:"",

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

  constructor(private fb: FormBuilder,private http:HttpClient,private requestService:RequestService,private route:ActivatedRoute) {
    this.myForm = this.fb.group({
      id:[""],
      areaOfInterest: new FormControl("",Validators.required),
      status: new FormControl("",Validators.required),
      startDate: new FormControl("",Validators.required),
      endDate: new FormControl("",Validators.required),
      description: new FormControl("",Validators.required),
      notes: new FormControl("",Validators.required),
      createdBy:"",
      updatedBy:"",
      resourceDTOS: this.fb.array([]),
    },
        {validator: [ CustomeDateValidators.fromToDate('startDate', 'endDate')]});
    console.log(this.myForm);

     let a =this.route.snapshot.paramMap.get('id');

    this.getOneRequest(a);



  }


  //will submit the data

  onSubmit(formData) {

    formData.updatedBy=this.currentEmail;

    this.http.post('http://localhost:8080/api/update-request', formData).subscribe(data=>{
      console.warn("data",data)
      let a='1'
      localStorage.setItem('testIfTrue',a)
      window.location.reload()
    },error => {
      document.getElementById("deleteButton1").click();
    });

  }

myFunction() {
    document.getElementById("stopSpinnerHere").click();
  }

  addNewResourceDTOS() {
    this.control = <FormArray>this.myForm.controls.resourceDTOS;
    this.control.push(
        this.fb.group({
          seniority: new FormControl(null,Validators.required),
          skillDTOS: this.fb.array([this.fb.group({
            skill: new FormControl('',Validators.required)
          })]),
          resourceNotes: this.fb.control(null,Validators.required),
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

  setResourceDTOS(data) {
    let control = <FormArray>this.myForm.controls.resourceDTOS;
    data.resourceDTOS.forEach(x => {
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
        skill: new FormControl(y.skill,Validators.required)
      }))
    })
    return arr;
  }



  getOneRequest(id) {
    this.http.get(`http://localhost:8080/api/requests/${id}`,id).subscribe((data) => {
      console.log(this.myForm);
      console.log(data);
      JSON.stringify(data);
      this.data=data[0];
      console.log(this.myForm)
      this.setResourceDTOS(data[0])
      console.log(this.myForm)
      this.myForm.patchValue(data[0]);



    }

    );


  }


  DeleteRequest(){
    this.requestService.deleteRequest(localStorage.getItem('requestId'));

  }

  get description() {
    return this.myForm.get('description');
  }
  get notes() {

    return this.myForm.get('notes');
  }

  get endDate() {
    return this.myForm.get('endDate');
  }
  get startDate() {
    return this.myForm.get('startDate');
  }



  onChange($event){
    this.currentStatusValue = $event.target.options[$event.target.options.selectedIndex].text;
  }
}
