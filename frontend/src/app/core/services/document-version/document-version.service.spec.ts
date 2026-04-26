import { TestBed } from "@angular/core/testing";
import { provideHttpClient } from "@angular/common/http";
import { provideHttpClientTesting } from "@angular/common/http/testing";
import { DocumentVersionService } from "./document-version.service";

describe("DocumentVersionService", () => {
  let service: DocumentVersionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        DocumentVersionService,
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });
    service = TestBed.inject(DocumentVersionService);
  });

  it("should be created", () => {
    expect(service).toBeTruthy();
  });
});