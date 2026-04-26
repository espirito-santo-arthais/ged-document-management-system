import { TestBed } from "@angular/core/testing";
import { provideHttpClient } from "@angular/common/http";
import { provideHttpClientTesting } from "@angular/common/http/testing";

import { DocumentService } from "./document.service";

describe("DocumentService (Placeholder)", () => {
  let service: DocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        DocumentService,
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });
    service = TestBed.inject(DocumentService);
  });

  it("should be created", () => {
    expect(service).toBeTruthy();
  });
});