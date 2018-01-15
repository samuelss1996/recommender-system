(deftemplate user
	(slot id)
	(slot name)
	(slot age)
	(slot sex
		(type SYMBOL)
		(allowed-symbols m w nil)
		(default nil)
	)
	(multislot purchased_products)
	(multislot products_tags)
)

(deftemplate product
	(slot id)
	(slot name)
	(multislot tags)
)

(deftemplate shop_order
	(slot user_id)
	(slot product_id)
)

(deftemplate recommendation
	(slot user_id)
	(slot product_id)
)

; TODO change some types to string
; TODO a minimum of five rules are required
; TODO maybe add something like "users like you are buying"

(defrule buy
	?sp <- (shop_order (user_id ?uid) (product_id ?pid))
	?u <- (user (id ?uid) (purchased_products $?pp) (products_tags $?upt))
	?p <- (product (id ?pid) (tags $?pt)) =>

	(retract ?sp)
	(modify ?u (purchased_products $?pp ?pid) (products_tags $?upt $?pt))
)

(defrule invalid_buy
	?sp <- (shop_order) =>

	(retract ?sp)
)

(defrule clear_recommendations
	?u <- (user (id ?uid) (purchased_products $?pp))
	?p <- (product (id ?pid))
	?r <- (recommendation (user_id ?uid) (product_id ?pid))
	(test (member$ ?pid $?pp)) =>

	(retract ?r)
)

(defrule update_recommendations
	?u <- (user (id ?uid) (purchased_products $?pp) (products_tags $?upt))
	?p <- (product (id ?pid) (tags $?pt))
	(test (and (not (member$ ?pid $?pp)) (subsetp $?pt $?upt))) =>

	(assert (recommendation (user_id ?uid) (product_id ?pid)))
)

(deffacts Data
	(user (id 1) (name Samuel) (age 21) (sex m))

	(product (id 1) (name Escoba) (tags limpieza hogar))
	(product (id 2) (name Lejia) (tags limpieza hogar))
)