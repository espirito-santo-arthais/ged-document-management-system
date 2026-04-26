import { ComponentFixture, TestBed } from "@angular/core/testing";
import { provideHttpClient } from "@angular/common/http";
import { provideHttpClientTesting } from "@angular/common/http/testing";
import { provideRouter } from "@angular/router";

import { DocumentListComponent } from "./document-list.component";
import { ShortIdPipe } from "../../shared/pipes/short-id-pipe";

describe("DocumentListComponent", () => {
  let component: DocumentListComponent;
  let fixture: ComponentFixture<DocumentListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      // Como é Standalone, o componente e o Pipe (se não for standalone) entram aqui
      imports: [DocumentListComponent, ShortIdPipe],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([]),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(DocumentListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});