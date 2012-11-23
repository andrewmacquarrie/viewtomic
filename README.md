viewtomic
=========

A really basic (AKA unfinished) Datomic GUI written in clojure.

Only supports viewing data at the current point in time, searching by entity ID or attribute name.

Uses the SeeSaw swing wrapper https://github.com/daveray/seesaw

Usage
========
Uses Leiningen https://github.com/technomancy/leiningen

You'll need to change the connection URL in the db.clj file to point to your datomic database. Then at the command line:

> lein deps

> lein run

Once the app starts you can search for entities either by attribute name or the :db/id of a specific entity.