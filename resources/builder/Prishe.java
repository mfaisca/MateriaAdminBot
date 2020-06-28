package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Prishe extends _Default{
	public Prishe() {
		super("Prishe", null);
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{8323};
				break;
			case HP:
				ids = new int[]{8324};
				break;
			case S1:
				ids = new int[]{8078, 8070, 8069};
				break;
			case S2:
				ids = new int[]{8075, 8076, 8074};
				break;
			case EX:
				ids = new int[]{8082};
				break;
			case AA:
				ids = new int[]{6653};
				break;
			case LD:
				ids = new int[]{};
				break;
			case BT:
				ids = new int[]{};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}