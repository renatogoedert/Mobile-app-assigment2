Discussion of all functionality, including, if any, 3rd party and/or

Google APIs used.

UML & Class Diagrams ( can be sketched with a pencil! )

UX / DX Approach Adopted

Git approach adopted and link to git project / access, if any

graph TD
  subgraph "Master Branch" 
    master
  end

  subgraph "Develop Branch" 
    develop
    subgraph "Components Branch"
      components
    end
    subgraph "Persistence Branch"
      persistence
    end
    subgraph "UX Branch"
      ux
    end
  end

  subgraph "Release Branch"
    release
  end

  develop -->|Merge| master
  components -->|Merge| develop
  persistence -->|Merge| develop
  ux -->|Merge| develop
  master -->|Tag & Merge| release


Personal Statement
