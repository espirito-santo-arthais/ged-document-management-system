import { TestBed } from "@angular/core/testing";

import { DocumentVersion, DocumentVersionService } from "./document-version.service";

describe("DocumentVersion", () => {
  let service: DocumentVersionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DocumentVersionService);
  });

  it("should be created", () => {
    expect(service).toBeTruthy();
  });
});
