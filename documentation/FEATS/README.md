# CoFFEE

[![License: MIT](https://img.shields.io/github/license/ramp-eu/feats.svg)](https://opensource.org/licenses/MIT)
<br/>
[![Documentation Status](https://readthedocs.org/projects/feats/badge/?version=latest)](https://feats.readthedocs.io/en/latest/?badge=latest)
[![Build badge](https://img.shields.io/travis/ramp-eu/FEATS.svg)](https://travis-ci.org/ramp-eu/FEATS/)
[![CII Best Practices](https://bestpractices.coreinfrastructure.org/projects/<project_id>/badge)](https://bestpractices.coreinfrastructure.org/projects/<project_id>)

CoFFEE is a component of the FEATS project.
This component has the responsibility to integrate information from ERPs.

This project is part of [DIH^2](http://www.dih-squared.eu/). For more information check the RAMP Catalogue entry for the
[components](https://github.com/ramp-eu).

## Contents

-   [Background](#background)
-   [Documentation](#documentation)
-   [License](#license)

## Background

Fiware-Enabled Autonomous Transport System (FEATS) is a project that the scope is automation in industry through a robot. This component - CoFFEE - is responsible to retrieve the industry work orders from ERP (currently only [SAP](https://www.sap.com/) is supported) and integrate them in FEATS system, through Orion.
[Orion](https://fiware-orion.readthedocs.io/en/master/) is a context broker that works in a publish/subscribe mechanism. This broker is part of the [FIWARE](https://www.fiware.org/) platform.

## Documentation

All documentation is available in `docs` folder. Check [documentation index](docs/index.md) to get all the information about the system architecture and API, and also about how to install and test the application.

## License

[MIT](LICENSE) © <TTE>
